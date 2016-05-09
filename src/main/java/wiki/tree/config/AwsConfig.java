package wiki.tree.config;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import wiki.tree.publish.AwsS3Template;

/**
 * @author chanwook
 */
@Configuration
@ImportResource(locations = "/applicationContext-aws.xml")
public class AwsConfig {

    @Bean
    public AwsS3Template s3Template(AmazonS3 s3) {
        return new AwsS3Template(s3);
    }
}
