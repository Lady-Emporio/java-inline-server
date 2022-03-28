package app.orm;



import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.support.ConnectionSource;

import app.ClearSky;
import app.models.MediaFiles;

public class Conn {
	public Conn() throws SQLException {
		String DATABASE_URL = "jdbc:sqlite::memory:";
		//String DATABASE_URL = "jdbc:h2:mem:account";

		ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);

		Dao<MediaFiles, String> dao = DaoManager.createDao(connectionSource, MediaFiles.class);

		TableUtils.createTableIfNotExists(connectionSource, MediaFiles.class);
		
		ClearSky.log("Good create table.");
	}
}
