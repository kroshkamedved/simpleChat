package client;


import java.io.IOException;

class UnaccesibleRoutException extends IOException {
    public UnaccesibleRoutException(String s) {
        super(s);
    }
}
