package greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {


    private final DaoConfig patientDaoConfig;
    private final PatientDao patientDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);


        patientDaoConfig = daoConfigMap.get(PatientDao.class).clone();
        patientDaoConfig.initIdentityScope(type);


        patientDao = new PatientDao(patientDaoConfig, this);

        registerDao(Patient.class, patientDao);
    }

    public void clear() {
        patientDaoConfig.getIdentityScope().clear();
    }


    public PatientDao getPatientDao() {
        return patientDao;
    }


}
