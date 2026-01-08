package info.jemsit.auth_service.data.dao;

import info.jemsit.auth_service.data.model.Token;

public interface TokenDAO {
    Token saveToken(Token token);
    Token findByToken(String token);
    void deleteToken(Token token);
}
