package util.ListCatch;

import java.util.List;
import java.util.ArrayList;

public class ListCatch<T> {
    /**
     * @author 3151301111
     * @date 2018/4/4 16:57
     * @version v3.2
     * 		  修改:线性查找函数get(int from, int num,Checker<T> checker)的查找方向变为降序
     * @var loopArray 循环数组
     * @var max_size 循环数组的容量
     * @var endID 当前最小ID
     * @var endID 当前最大ID+1
     * #当前ID的区间范围为[beginID,endID)【左闭右开】
     * #获取ID所在下标是通过ID%max_size
     * #已用空间用 endID - beginID 得到
     * #遍历用类似for(i=beginID;i< endID;i++)
     *                  loopArray.get(i%max_size)实现
     */
    private Object[] loopArray;
    private int max_size;
    private int beginID, endID;
    /**
     * 构造函数
     *
     * @param max_size      最大容量
     * @param beignID       起始ID
     */
    public ListCatch(int max_size, int beignID) {
        this.max_size = max_size;
        this.beginID = beignID;
        this.endID = beignID;
        this.loopArray = new Object[max_size];
    }

    /**
     * 往尾部插入数据并返回获得的id,如果数组已满，自动弹出头部
     *
     * @param data 数据
     * @return 获得的id,
     */
    public int pushBack(T data) {
        if (endID - beginID == max_size)
            ++beginID;
        loopArray[endID%max_size]=data;
        ++endID;
        return endID - 1;
    }

    /**
     * @return 被弹出头部数据
     * 强制类型转换未加检测!!!
     */
    @SuppressWarnings("unchecked")
    public T popFront() {
        if (beginID < endID) {
            ++beginID;
            return (T)loopArray[(beginID - 1) % max_size];
        }
        return null;
    }
    /**
     * @return 当前数组已用空间大小
     */
    public int size() {
        return endID - beginID;
    }
    /**
     * 从指定位置开始检索，直至到begin或者找到num个符合要求的数据。
     * 强制类型转换未加检测!!!
     * @param from 从第from+1个数据开始按id降序查找
     * @param num 需要的符合要求的数据数量
     * @return 满足条件的数据列表
     */
    @SuppressWarnings("unchecked")
    public List<T> get(int from, int num) {
        return get(from, num, new Checker<T>() {
            @Override
            public boolean check(T data) {
                return true;
            }
        });
    }
    /**
     * 从指定位置开始检索，直至到begin或者找到num个符合要求的数据。
     * 强制类型转换未加检测!!!
     * @param from 从第from+1个满足条件的数据开始按id降序查找
     * @param num 需要的符合要求的数据数量
     * @param checker 检测方法
     * @return 满足条件的数据列表
     */
    @SuppressWarnings("unchecked")
    public List<T> get(int from, int num,Checker<T> checker) {
        List<T> resultList = new ArrayList<T>(num);
        int number = 0;
        for(int id=getMaxID();id>=getMinID();id--){
            T t = get(id);
            if(checker.check(t)){
                number ++ ;
                if(number <= from) continue;
                else{
                    resultList.add(t);
                    if(resultList.size()>=num) break;
                }
            }
        }

        /*if(from>=beginID&&size()>0)
        {
            int index=Math.min(from, endID-1);
            int i=index%max_size;
            while(index>=beginID&&num>0)
            {
                if(checker.check((T)loopArray[i]))
                {
                    --num;
                    resultList.add((T)loopArray[i]);
                }
                --i;
                --index;
                if(i<0)
                    i+=max_size;
            }
        }*/
        return resultList;
    }
    /**
     * 获得指定id的数据，若id不存在返回null
     * 强制类型转换未加检测!!!
     * @param id  数据的id
     * @return 指定id的数据
     */
    @SuppressWarnings("unchecked")
    public T get(int id) {
        if (has(id)) {
            return (T) loopArray[id % max_size];
        }
        return null;
    }
    /**
     *
     * @return 当前循环数组内的最小有效id
     */
    public int getMinID() {
        return beginID;
    }
    /**
     * 强制类型转换未加检测!!!
     * @return 当前循环数组内的最大有效id
     */
    public int getMaxID() {
        return endID - 1;
    }
    /**
     * 判断id是否存在
     * @param id 要查询的id
     * @return 若id存在返回true，否则返回false
     */
    public boolean has(int id) {
        return beginID <= id && id < endID;
    }

    /**
     * 下次插入的id
     * @return 下次插入的id
     */
    public int getNextID(){
        return endID;
    }

}
