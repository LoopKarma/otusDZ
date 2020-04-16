package orm.service;

class DbServiceException extends RuntimeException {
    DbServiceException(Exception e) {
        super(e);
    }
}
