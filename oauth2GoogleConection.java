import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everis.eva.service.dto.nlp.CognitiveServiceRequest;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;

@Component
public class OAuthClient {

	@Autowired
	private Logger logger;

	public String autorizationToken(CognitiveServiceRequest request) throws IOException {
		final String key = new Gson().toJson(request.getMetadata());
		logger.info("Key: {}", key);
		final InputStream input = new ByteArrayInputStream(key.getBytes());
		GoogleCredentials credentials = GoogleCredentials.fromStream(input);
		credentials = credentials.createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform",
				"https://www.googleapis.com/auth/dialogflow"));
		credentials.refreshAccessToken();

		logger.info("Credentials: {}", credentials);

		final AccessToken accessToken = credentials.refreshAccessToken();
		logger.info("AccessToken: {}", accessToken);

		return accessToken.getTokenValue();
	}
}
