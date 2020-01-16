package translator;

import com.google.cloud.translate.v3beta1.LocationName;
import com.google.cloud.translate.v3beta1.TranslateTextRequest;
import com.google.cloud.translate.v3beta1.TranslateTextResponse;
import com.google.cloud.translate.v3beta1.TranslationServiceClient;

/**
 * see README.md for detailed description
 */
@Deprecated
public abstract class StreamTranslator{
    /**
     * Translates a given text to a target language.
     * https://cloud.google.com/translate/docs/quickstart-client-libraries-v3
     *
     * the given code was copied from the link below, it works with one of
     * google's cloud services but fails with an IOException because of missing
     * application credentials. apparently it is required to create an google
     * developer account in order to authenticate with the google cloud and
     * use this library. (second and third link below)
     *
     * cloud services account was not created, class marked as deprecated and no
     * longer in use
     *
     * https://cloud.google.com/docs/authentication/production?hl=de
     * https://cloud.google.com/docs/authentication/getting-started?hl=de
     *
     *  @param projectId - Id of the project.
     * @param location - location name.
     * @param text - Text for translation.
     * @param sourceLanguageCode - Language code of text. e.g. "en"
     * @param targetLanguageCode - Language code for translation. e.g. "sr"
     */
    private static void translateText(
            String projectId,
            String location,
            String text,
            String sourceLanguageCode,
            String targetLanguageCode) {
        try (TranslationServiceClient translationServiceClient = TranslationServiceClient.create()) {

            LocationName locationName =
                    LocationName.newBuilder().setProject(projectId).setLocation(location).build();

            TranslateTextRequest translateTextRequest =
                    TranslateTextRequest.newBuilder()
                            .setParent(locationName.toString())
                            .setMimeType("text/plain")
                            .setSourceLanguageCode(sourceLanguageCode)
                            .setTargetLanguageCode(targetLanguageCode)
                            .addContents(text)
                            .build();

            // Call the API
            TranslateTextResponse response = translationServiceClient.translateText(translateTextRequest);
            System.out.format(
                    "Translated Text: %s", response.getTranslationsList().get(0).getTranslatedText());

        } catch (Exception e) {
            throw new RuntimeException("Couldn't create client.", e);
        }
    }

    /**
     * test driver for translateText method written above
     */
    public static void main() {
        translateText("test1", "home", "Hello world", "en", "sr");
        translateText("test2", "somewhere", "Hallo Welt", "de", "en");
    }
}