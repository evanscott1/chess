package dataaccess;


import model.UserData;
import java.util.Collection;


public class MySQLUserDAO extends MySQLBaseDAO implements UserDataAccess {

    @Override
    public UserData addUserData(UserData userData) throws DataAccessException {
        String hashedPassword = hashStringBCrypt(userData.password());
        UserData hashedUser = new UserData(userData.username(), hashedPassword, userData.email());
        addT(hashedUser);
        return userData;
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        return getT("username", username, UserData.class);
    }

    @Override
    public Collection<UserData> listUserDatas() throws DataAccessException {
        return listTs(UserData.class);
    }

    @Override
    public void deleteAllUserDatas() throws DataAccessException {
        deleteAllTs();
    }

    @Override
    protected String getTableName() {
        return "user";
    }


    @Override
    protected String[] getCreateStatements() {
        return new String[] {
                """
            CREATE TABLE IF NOT EXISTS  user (
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(256) NOT NULL,
                `password` char(60) NOT NULL,
                `email` varchar(256) NOT NULL,
                `json` TEXT DEFAULT NULL,
                PRIMARY KEY (`id`),
                INDEX(username),
                INDEX(password)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs
            """
        };
    }

}
