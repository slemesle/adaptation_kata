package converters;

import static builders.model.AudioEventModelTestBuilder.audio;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.junit.Test;
import api.domain.AudioEvent;
import builders.persistent.UserEntityTestBuilder;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import persistent.AudioEventEntity;
import persistent.UserEntity;
import services.URLService;

public class AudioConverterTest {

    @Test
    public void should_convert_model_to_entity() throws MalformedURLException {
        // Given
        UUID id = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
        Long userId = 12345L;
        URL url = new URL("file://url");
        String encryptedFilename = "encryptedFilename";
        String xmppId = "1654";
        final int duration = 10;

        UserEntity userEntity = new UserEntityTestBuilder().userId(userId).build();

        final URLService urlService = mock(URLService.class);
        when(urlService.fromUrl(url)).thenReturn(encryptedFilename);

        AudioEvent model = audio()
                .serverId(id)
                .xmppId(xmppId)
                .url(url)
                .duration(duration)
                .build();

        AudioConverter audioConverter= new AudioConverter(urlService);
        // When
        final AudioEventEntity file = audioConverter.toEntity(model, userEntity);
        // Then
        assertThat(file.getId()).isEqualTo(id);
        assertThat(file.getUserId()).isEqualTo(userId);
        assertThat(file.getXmppId()).isEqualTo(xmppId);
        assertThat(file.getFilename()).isEqualTo(encryptedFilename);
        assertThat(file.getDuration()).isEqualTo(duration);
    }

}