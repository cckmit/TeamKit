package org.team4u.kit.core.action;

public interface Each<T> {

    /**
     * 回调接口
     *
     * @param index  当前项目的下标
     * @param ele    当前项目
     * @param length 集合总长度，当然并不是所有的迭代器都会给出总长度的，-1 表示未知
     * @throws ExitLoop      抛出这个异常，表示你打算退出循环
     * @throws ContinueLoop  抛出这个异常，表示你打算停止递归，但是不会停止循环
     * @throws LoopException 抛出这个异常，表示你打算退出循环，并且将会让迭代器替你将包裹的异常抛出
     */
    void invoke(int index, T ele, int length) throws ExitLoop, ContinueLoop, LoopException;


    /**
     * 退出循环
     */
    class ExitLoop extends RuntimeException {
    }

    /**
     * 继续循环，如果正在递归，则停止继续递归
     */
    class ContinueLoop extends RuntimeException {
    }

    class LoopException extends RuntimeException {
        public LoopException(Throwable cause) {
            super(cause);
        }
    }
}
