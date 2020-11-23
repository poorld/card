package ooo.poorld.mycard.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: teenyda
 * date: 2020/11/21
 * description:
 */
@Entity
public class User {

    private String username;
    private String password;
    private String alias;
    @Generated(hash = 1794730411)
    public User(String username, String password, String alias) {
        this.username = username;
        this.password = password;
        this.alias = alias;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

}
