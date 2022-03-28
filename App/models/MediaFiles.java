package app.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MediaFiles")
public class MediaFiles {
	@DatabaseField(dataType=DataType.INTEGER,  generatedId = true, columnName = "id")
	public int id;
	@DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = "bytes")
	public byte[] bytes;

	@DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "name")
	public String name;

	@DatabaseField(canBeNull = false, dataType = DataType.DATE, columnName = "create_at")
	public String create_at;
	
	public MediaFiles() {}

}