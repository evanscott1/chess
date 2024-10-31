package dataaccess;

import exception.ResponseException;
import model.AuthData;
import service.BadRequestException;

import java.util.Collection;

public class MemoryAuthDAO extends MemoryBaseDAO<AuthData> implements AuthDataAccess {
    @Override
    public AuthData addAuthData(AuthData authData) throws ResponseException {
        if (authData.authToken() == null || authData.username() == null) {
            throw new BadRequestException("DataAuth fields cannot be null");
        }
        return addT(authData);
    }

    @Override
    public AuthData getAuthData(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new BadRequestException("Auth token cannot be empty or null");
        }
        return getT("authToken", authToken);
    }

    @Override
    public Collection<AuthData> listAuthDatas() throws ResponseException {
        return listTs();
    }

    @Override
    public void deleteAuthData(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new BadRequestException("Auth token cannot be empty or null");
        }
        deleteT("authToken", authToken);
    }

    @Override
    public void deleteAllAuthDatas() throws ResponseException {
        deleteAllTs();
    }
}
