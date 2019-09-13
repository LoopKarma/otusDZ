package department.memento;

import org.apache.commons.beanutils.BeanUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

public interface StateHistoryBehavior {
    default String backupState() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(this);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Serialization of object " + this.toString() + " failed");
        }
    }

    default void restoreLastState(String state) {
        byte[] data = Base64.getDecoder().decode(state);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));) {

            BeanUtils.copyProperties(this, ois.readObject());
        } catch (ClassNotFoundException e) {
            System.out.print("ClassNotFoundException occurred.");
        } catch (IOException e) {
            System.out.print("IOException occurred.");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
