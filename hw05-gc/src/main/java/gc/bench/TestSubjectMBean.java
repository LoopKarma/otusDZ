package gc.bench;

public interface TestSubjectMBean {
  void run() throws OutOfMemoryError;
  long getLoopCounter();
  void setSize(int size);
  int getSize();
}
