package mikhail.shell.gleamy.dao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@Getter
public abstract class AbstractDAO {
    protected final ApplicationContext appContext;
    protected final JdbcTemplate jdbc;
    @Autowired
    public AbstractDAO(ApplicationContext appContext, 
            JdbcTemplate jdbc)
    {
        this.appContext = appContext;
        this.jdbc = jdbc;
    }
}
