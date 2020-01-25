package translator;

import com.google.cloud.translate.v3beta1.LocationName;
import com.google.cloud.translate.v3beta1.TranslateTextRequest;
import com.google.cloud.translate.v3beta1.TranslateTextResponse;
import com.google.cloud.translate.v3beta1.TranslationServiceClient;

/**
 * see README.md within this directory for detailed description of this class and the intention behind it
 * @author Pichler Martin
 * @since january 2020
 * @see com.google.cloud.translate.v3beta1
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
     * @param projectId - Id of the project.
     * @param location - location name.
     * @param text - Text for translation.
     * @param sourceLanguageCode - Language code of text. e.g. "en"
     * @param targetLanguageCode - Language code for translation. e.g. "sr"
     * @author Pichler Martin
     * @since january 2020
     */
    private static void translateText(
            String projectId,
            String location,
            String text,
            String sourceLanguageCode,
            String targetLanguageCode) {
        try (TranslationServiceClient translationServiceClient = TranslationServiceClient.create()) {

            //  set the location for the translation request to the current project directory
            LocationName locationName =
                    LocationName.newBuilder().setProject(projectId).setLocation(location).build();

            //  build a translation request
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
            //  in case there was some error with the network this is mostly what happens
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