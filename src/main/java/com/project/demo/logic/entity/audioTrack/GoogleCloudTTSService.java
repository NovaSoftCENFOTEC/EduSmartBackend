package com.project.demo.logic.entity.audioTrack;


import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GoogleCloudTTSService {

    @Value("${gcp.credentials.location}")
    private Resource credentialsResource;

    public byte[] convertTextToMp3(String text) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsResource.getInputStream());

        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("es-US")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();
            return audioContents.toByteArray();
        }
    }
}