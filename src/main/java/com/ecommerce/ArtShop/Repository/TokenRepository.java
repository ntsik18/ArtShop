package com.ecommerce.ArtShop.Repository;


import com.ecommerce.ArtShop.Model.Token.Token;
import com.ecommerce.ArtShop.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("Select t from Token t inner join _User u on t.user.id=u.id" +
            " where u.id =:id and (t.expired=false or t.revoked = false)"
    )
    List<Token> findAllValidTokens(long id);

    Optional<Token> findByToken(String Token);

    Optional<Token> findByUser(Optional<User> user);


}
