package com.project.demo.logic.entity.audioTrack;


import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Servicio para convertir texto en audio utilizando Google Cloud Text-to-Speech.
 */
@Service
public class GoogleCloudTTSService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleCloudTTSService.class);

    @Value("${gcp.credentials.location}")
    private Resource credentialsResource;

    private final static String LANGUAGE_CODE = "es-US";

    /**
     * Convierte un texto en un archivo MP3 usando Google Cloud TTS.
     * @param text texto a convertir
     * @param voiceType tipo de voz a utilizar
     * @return arreglo de bytes del archivo MP3 generado
     * @throws IOException si ocurre un error durante la conversión
     */
    public byte[] convertTextToMp3(String text, VoiceTypeEnum voiceType) throws IOException {
        logger.info("Empezando la conversión de texto a MP3 con Google Cloud TTS");
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsResource.getInputStream());

        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            VoiceSelectionParams.Builder voiceBuilder = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(LANGUAGE_CODE);

            if (voiceType.equals(VoiceTypeEnum.FEMALE)) {
                voiceBuilder.setSsmlGender(SsmlVoiceGender.FEMALE);
            } else if (voiceType.equals(VoiceTypeEnum.MALE)) {
                voiceBuilder.setSsmlGender(SsmlVoiceGender.MALE);
            } else {
                voiceBuilder.setSsmlGender(SsmlVoiceGender.NEUTRAL);
            }
            VoiceSelectionParams voice = voiceBuilder.build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();
            logger.info("Conversión de texto a MP3 completada exitosamente");
            return audioContents.toByteArray();
        } catch (IOException e) {
            logger.error("Error durante la conversión de texto a MP3: {}", e.getMessage());
            throw e;
        }
    }
}