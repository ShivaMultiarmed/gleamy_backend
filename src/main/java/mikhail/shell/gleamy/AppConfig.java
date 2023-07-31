package mikhail.shell.gleamy;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class AppConfig {
    @Bean
    public JdbcTemplate jdbc()
    {
        JdbcTemplate jdbc = new JdbcTemplate();
        jdbc.setDataSource(dataSource());
        return jdbc;
    }
    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/gleamy");
        ds.setUsername("root");
        ds.setPassword("");
        return ds;
    }
}
