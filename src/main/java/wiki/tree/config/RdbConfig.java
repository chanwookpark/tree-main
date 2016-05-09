package wiki.tree.config;

import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.cloud.aws.jdbc.config.annotation.RdsInstanceConfigurer;
import org.springframework.cloud.aws.jdbc.datasource.TomcatJdbcDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author chanwook
 */
@Configuration
public class RdbConfig {

    @Profile("aws")
    @EnableRdsInstance(dbInstanceIdentifier = "treewiki", password = "${aws.rds.password}", readReplicaSupport = false)
    static class AwsRdsConfig {
        @Bean
        public RdsInstanceConfigurer rdsConfigurer() {
            return () -> {
                final TomcatJdbcDataSourceFactory factory = new TomcatJdbcDataSourceFactory();
                factory.setInitialSize(5);
                factory.setValidationQuery("SELECT 1 FROM DUAL");
                return factory;
            };
        }
    }

    @Profile("local")
    static class LocalRdbConfig {
        // 로컬 일때는 프로퍼티 파일에서 기본 데이터소스로 로딩..(쉽게..)
    }
}
