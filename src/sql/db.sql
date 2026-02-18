CREATE USER invoice_db_manager with ENCRYPTED password '4567';

CREATE DATABASE invoice_db ;

GRANT CONNECT on database invoice_db to invoice_db_manager ;

\c invoice_db;

grant create on schema public to invoice_db_manager;

alter default privileges in schema public
grant select , insert ,update ,delete on tables to invoice_db_manager;

alter default privileges in schema public
grant usage , select ,update on sequences to invoice_db_manager;