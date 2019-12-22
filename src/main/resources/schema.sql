DROP TABLE if EXISTS tree;
CREATE TABLE tree (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL DEFAULT '',
    lft INT NOT NULL DEFAULT 1,
    rgt INT NOT NULL DEFAULT 2,
    depth INT NOT NULL DEFAULT 1,
    INDEX idx_lft(lft)
)ENGINE=INNODB DEFAULT CHARSET=UTF8;
