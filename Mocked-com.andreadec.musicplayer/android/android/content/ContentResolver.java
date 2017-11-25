package android.content;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import gov.nasa.jpf.symbc.Debug;

public class ContentResolver {

	SQLiteDatabase sql;

	public ContentResolver() {
		this.sql = new SQLiteDatabase(null, 0, null, null);
	}

	public Cursor query(String externalContentUri, String[] projection, String where, String[] selectionArgs,
			String orderBy) {
		//Debug.printSymbolicRef(externalContentUri,"VALUE OF externalContentUri in query method!!!");
		return sql.query(externalContentUri, projection, where, selectionArgs, null, null, orderBy);
	}

}
