package emmet.sales.entity.pi;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import emmet.core.data.repository.EmployeeRepository;

public class DatePrifixIdGenerator extends SequenceGenerator {

	private static final String ID_PRIFIX = "PI";
	private static final String SERIAL_NUM_FORAMT = "%02d";
	private static final String SEQUENCE_TABLE_NAME_PREFIX = "order_sequence_";

	private static final Log log = LogFactory.getLog(DatePrifixIdGenerator.class);

	
	@Autowired
	EmployeeRepository repository;
	
	private String sequenceTableName;

	@Override
	public Serializable generate(SessionImplementor session, Object obj) {
		
		sequenceTableName = SEQUENCE_TABLE_NAME_PREFIX + getDatePrefix();

		Connection connection = session.connection();
		PreparedStatement ps = null;

		try {
			ResultSet rs = connection.getMetaData().getTables(null, null, sequenceTableName, null);
			if (!rs.next()) {
				log.info("The sequence table [" + sequenceTableName + "] not existed, create one.");

				ps = connection.prepareStatement("CREATE SEQUENCE " + sequenceTableName + "  MINVALUE 1");
				ps.execute();
			
				log.info("The sequence table [" + sequenceTableName + "] created.");

			}
		} catch (SQLException e1) {
			log.error(e1);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}

		try {

			ps = connection.prepareStatement("SELECT nextval ('" + sequenceTableName + "') as nextval");
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("nextval");
				String code = ID_PRIFIX +  getDatePrefix() +String.format(SERIAL_NUM_FORAMT, id);
				return code;
			}

		} catch (SQLException e) {
			throw new HibernateException("Unable to generate ID");
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}

		return null;

	}
	
	private String getDatePrefix() {
        DateFormat df = new SimpleDateFormat("YYYYMMdd");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
}
