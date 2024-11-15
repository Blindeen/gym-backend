package project.gym.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.walletobjects.Walletobjects;
import com.google.api.services.walletobjects.WalletobjectsScopes;
import com.google.api.services.walletobjects.model.Barcode;
import com.google.api.services.walletobjects.model.GenericObject;
import com.google.api.services.walletobjects.model.LocalizedString;
import com.google.api.services.walletobjects.model.TextModuleData;
import com.google.api.services.walletobjects.model.TranslatedString;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

@Service
public class GoogleWalletService {
    private final String KEY_FILE_PATH = "google_wallet_service_key.json";

    @Value("${google.wallet.issuer.id}")
    private String issuerId;

    @Value("${google.wallet.class.suffix}")
    private String classSuffix;

    private GoogleCredentials credentials;
    private Walletobjects service;

    public GoogleWalletService() throws Exception {
        auth();
    }

    private void auth() throws Exception {
        ClassPathResource resource = new ClassPathResource(KEY_FILE_PATH);
        credentials = GoogleCredentials.fromStream(new FileInputStream(resource.getFile()))
                .createScoped(List.of(WalletobjectsScopes.WALLET_OBJECT_ISSUER));
        credentials.refresh();

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        service = new Walletobjects.Builder(
                httpTransport,
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("FITSPHERE_BACKEND")
                .build();
    }

    private boolean doesObjectExist(String objectSuffix) throws IOException {
        try {
            service.genericobject().get(String.format("%s.%s", issuerId, objectSuffix)).execute();
            return true;
        } catch (GoogleJsonResponseException ex) {
            if (ex.getStatusCode() != 404) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    private GenericObject getObjectRequest(String objectSuffix) throws IOException {
        GenericObject existingPassObject = null;

        try {
            existingPassObject = service.genericobject().get(String.format("%s.%s", issuerId, objectSuffix)).execute();
        } catch (GoogleJsonResponseException ex) {
            ex.printStackTrace();
        }

        return existingPassObject;
    }

    private GenericObject createGenericObject(String objectSuffix, String passUuid, String firstName, String lastName) {
        LocalizedString cardTitle = new LocalizedString();
        cardTitle.setDefaultValue(new TranslatedString().setLanguage("en-US").setValue("Pass"));
        cardTitle.setTranslatedValues(List.of(new TranslatedString().setLanguage("pl-PL").setValue("Karnet")));

        LocalizedString header = new LocalizedString();
        header.setDefaultValue(new TranslatedString().setLanguage("en-US").setValue("FitSphere"));

        Barcode barcode = new Barcode().setType("QR_CODE").setValue(passUuid);

        TextModuleData memberTextModule = new TextModuleData();
        memberTextModule.setId("member");
        memberTextModule.setHeader("Member");

        LocalizedString textModuleHeader = new LocalizedString();
        textModuleHeader.setDefaultValue(new TranslatedString().setLanguage("en-US").setValue("Member"));
        textModuleHeader.setTranslatedValues(List.of(new TranslatedString().setLanguage("pl-PL").setValue("Członek")));

        memberTextModule.setLocalizedHeader(textModuleHeader);
        memberTextModule.setBody(String.format("%s %s", firstName, lastName));

        GenericObject newObject = new GenericObject()
                .setId(String.format("%s.%s", issuerId, objectSuffix))
                .setClassId(String.format("%s.%s", issuerId, classSuffix))
                .setState("ACTIVE")
                .setCardTitle(cardTitle)
                .setHeader(header)
                .setTextModulesData(List.of(memberTextModule))
                .setBarcode(barcode)
                .setHexBackgroundColor("#526FFF");

        return newObject;
    }

    private GenericObject updateGenericObject(GenericObject genericObject, String firstName, String lastName) {
        TextModuleData memberTextModule = genericObject.getTextModulesData().get(0);
        memberTextModule.setBody(String.format("%s %s", firstName, lastName));

        return genericObject;
    }

    private void sendCreateObjectRequest(String objectSuffix, String passUuid, String firstName, String lastName)
            throws IOException {
        GenericObject newObject = createGenericObject(objectSuffix, passUuid, firstName, lastName);
        try {
            service.genericobject().insert(newObject).execute();
        } catch (GoogleJsonResponseException ex) {
            ex.printStackTrace();
        }
    }

    private void sendUpdateObjectRequest(String objectSuffix, GenericObject updatedObject) throws IOException {
        service.genericobject().update(String.format("%s.%s", issuerId, objectSuffix), updatedObject).execute();
    }

    private String createJWTObject(String objectSuffix, String passUuid, String firstName, String lastName) {
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", ((ServiceAccountCredentials) credentials).getClientEmail());
        claims.put("aud", "google");
        claims.put("origins", List.of("*"));
        claims.put("typ", "savetowallet");

        HashMap<String, Object> payload = new HashMap<String, Object>();

        GenericObject newObject = createGenericObject(objectSuffix, passUuid, firstName, lastName);
        payload.put("genericObjects", List.of(newObject));
        claims.put("payload", payload);

        Algorithm algorithm = Algorithm.RSA256(null,
                (RSAPrivateKey) ((ServiceAccountCredentials) credentials).getPrivateKey());
        String token = JWT.create().withPayload(claims).sign(algorithm);

        return token;
    }

    public String generateGoogleWalletPass(String objectSuffix, String passUuid, String firstName, String lastName)
            throws IOException {
        credentials.refreshIfExpired();
        if (!doesObjectExist(objectSuffix)) {
            sendCreateObjectRequest(objectSuffix, passUuid, firstName, lastName);
        }

        String token = createJWTObject(objectSuffix, passUuid, firstName, lastName);
        return token;
    }

    @Async
    public void updateGoogleWalletPass(String objectSuffix, String firstName, String lastName) {
        try {
            credentials.refreshIfExpired();
            GenericObject existingPassObject = getObjectRequest(objectSuffix);
            if (existingPassObject != null) {
                existingPassObject = updateGenericObject(existingPassObject, firstName, lastName);
                System.out.println(existingPassObject.getTextModulesData().get(0).getBody());
                sendUpdateObjectRequest(objectSuffix, existingPassObject);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
