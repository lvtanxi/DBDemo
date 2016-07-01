package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * User: 吕勇
 * Date: 2016-06-12
 * Time: 10:41
 * Description:
 */
public class TestBean2Dao extends AbstractDao<Patient, String> {
    public TestBean2Dao(DaoConfig config) {
        super(config);
    }

    public TestBean2Dao(DaoConfig config, AbstractDaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected Patient readEntity(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected String readKey(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected void readEntity(Cursor cursor, Patient entity, int offset) {

    }

    @Override
    protected void bindValues(SQLiteStatement stmt, Patient entity) {

    }

    @Override
    protected String updateKeyAfterInsert(Patient entity, long rowId) {
        return null;
    }

    @Override
    protected String getKey(Patient entity) {
        return null;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return false;
    }
}
