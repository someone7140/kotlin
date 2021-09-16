CREATE TABLE auth_users (
  id VARCHAR2(100),
  auth_type VARCHAR2(100),
  refresh_token VARCHAR2(1000),
  refresh_token_expire_time NUMBER,
  CONSTRAINT pk_user PRIMARY KEY(id, auth_type)
);

CREATE TABLE register_pdfs (
  id VARCHAR2(100),
  title VARCHAR2(1000),
  url VARCHAR2(1000),
  type VARCHAR2(100),
  user_id VARCHAR2(100),
  register_time NUMBER,
  CONSTRAINT pk_register_pdfs PRIMARY KEY(id)
);

CREATE INDEX register_pdfs_user_id_index ON register_pdfs(user_id);
