/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

package src.parser;

public interface CharSource {
    boolean hasNext();
    char next();
    void saveChar();
    char returnChar();
}
