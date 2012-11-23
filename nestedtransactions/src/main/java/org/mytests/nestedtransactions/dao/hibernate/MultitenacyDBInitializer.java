package org.mytests.nestedtransactions.dao.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.HSQLDialect;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Initializer for database.
 * For multi tenacy configuration, the update database doesn't work.
 * 
 * @author Iulia
 *
 */
public class MultitenacyDBInitializer {
	
	private LocalSessionFactoryBean sessionFactoryBean;
	private Map<String, DataSource> dataSourceMap;

	public void initialiseBD() {
		Configuration cfg = sessionFactoryBean.getConfiguration();
		final String[] createScripts = cfg
				.generateSchemaCreationScript(new HSQLDialect());
		
		for(final DataSource dataSource : dataSourceMap.values()){
			TransactionTemplate transactionTemplate = new TransactionTemplate(
					new DataSourceTransactionManager(dataSource));
			transactionTemplate.execute(new TransactionCallback<Boolean>() {
	

				public Boolean doInTransaction(TransactionStatus status) {
					JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

	
					for (int i = 0; i < createScripts.length; i++) {
						String script = createScripts[i].trim();
						try {
							jdbcTemplate.execute(script);
						} catch(DataIntegrityViolationException e){
							//ignore "Table/View ... already exists in Schema ..."
						} catch (DataAccessException e) {
							throw e;
							/*
							 * if (ignoreFailedDrop &&
							 * script.toLowerCase().startsWith("drop")) {
							 * logger.debug("DROP script failed (ignoring): " +
							 * script); } else { throw e; }
							 */
						}
					}
	
					return true;
				}

		});
		}

	}

	public void setSessionFactoryBean(LocalSessionFactoryBean sessionFactoryBean) {
		this.sessionFactoryBean = sessionFactoryBean;
	}

	public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}
	
	
}
