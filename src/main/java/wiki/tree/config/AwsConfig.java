package wiki.tree.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import wiki.tree.publish.AwsS3Template;

/**
 * @author chanwook
 */
@Profile("aws")
@Configuration
@ImportResource(locations = "/applicationContext-aws.xml")
public class AwsConfig {

    @Bean
    public AwsS3Template s3Template(ResourceLoader resourceLoader) {
        return new AwsS3Template(resourceLoader);
    }
}
