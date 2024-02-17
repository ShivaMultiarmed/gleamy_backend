package mikhail.shell.gleamy;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class AppConfig {
    @Bean
    public DataSource dataSource()
    {
		//System.setProperty("javax.net.ssl.trustStore", "/home/mikhail_shell/.mysql/YATrustStore");
		//System.setProperty("javax.net.ssl.trustStorePassword", "BlackWater");
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //ds.setUrl("jdbc:mysql://rc1b-kt5nph9yudp43u71.mdb.yandexcloud.net:3306/db1?useSSL=true");
        ds.setUrl("jdbc:mysql://localhost:3306/gleamy_db?useSSL=true");
        ds.setUsername("root");
        //ds.setPassword("BlackWater");
        ds.setPassword("");
        return ds;
    }
}
