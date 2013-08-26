package jo.sm.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jo.vecmath.Point3i;
import jo.vecmath.Point3s;

public class SparseMatrix<T>
{
    private Map<Integer,Map<Integer,Map<Integer,T>>> mMatrix;
    
    public SparseMatrix()
    {
        mMatrix = new HashMap<Integer, Map<Integer,Map<Integer,T>>>();
    }
    
    public SparseMatrix(SparseMatrix<T> original)
    {
        this();
        set(original);
    }
    
    public void addAll(SparseMatrix<T> original)
    {
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i p = i.next();
            set(p, original.get(p));
        }
    }
    
    public void set(SparseMatrix<T> original)
    {
        mMatrix.clear();
        addAll(original);
    }
    
    public void set(int x, int y, int z, T val)
    {
        Map<Integer,Map<Integer,T>> xrow = mMatrix.get(x);
        if (xrow == null)
        {
            xrow = new HashMap<Integer, Map<Integer,T>>();
            mMatrix.put(x, xrow);
        }
        Map<Integer,T> yrow = xrow.get(y);
        if (yrow == null)
        {
            yrow = new HashMap<Integer, T>();
            xrow.put(y,  yrow);
        }
        if (val == null)
            yrow.remove(z);
        else
            yrow.put(z,  val);
    }
    
    public T get(int x, int y, int z)
    {
        Map<Integer,Map<Integer,T>> xrow = mMatrix.get(x);
        if (xrow == null)
            return null;
        Map<Integer,T> yrow = xrow.get(y);
        if (yrow == null)
            return null;
        return yrow.get(z);        
    }
    
    public boolean contains(int x, int y, int z)
    {
        return get(x, y, z) != null;
    }
    
    public void set(Point3i v, T val)
    {
        set(v.x, v.y, v.z, val);
    }
    
    public T get(Point3i v)
    {
        return get(v.x, v.y, v.z);
    }
    
    public T get(Point3s v)
    {
        return get(v.x, v.y, v.z);
    }
    
    public boolean contains(Point3i v)
    {
        return get(v.x, v.y, v.z) != null;
    }
    
    public void getBounds(Point3i lower, Point3i upper)
    {
        boolean first = true;
        for (Integer x : mMatrix.keySet())
        {
            Map<Integer,Map<Integer,T>> xrow = mMatrix.get(x);
            for (Integer y : xrow.keySet())
            {
                Map<Integer,T> yrow = xrow.get(y);
                for (Integer z : yrow.keySet())
                    if (contains(x, y, z))
                    {
                        if (first)
                        {
                            lower.x = x;
                            upper.x = x;
                            lower.y = y;
                            upper.y = y;
                            lower.z = z;
                            upper.z = z;
                            first = false;
                        }
                        else
                        {
                            lower.x = Math.min(lower.x, x);
                            upper.x = Math.max(upper.x, x);
                            lower.y = Math.min(lower.y, y);
                            upper.y = Math.max(upper.y, y);
                            lower.z = Math.min(lower.z, z);
                            upper.z = Math.max(upper.z, z);
                        }
                    }
            }
        }
    }
    
    public Iterator<Point3i> iterator()
    {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        getBounds(lower, upper);
        return new CubeIterator(lower, upper);
    }
    
    public Iterator<Point3i> iteratorNonNull()
    {
        return new NonNullIterator();
    }
    
    public int size()
    {
        int size = 0;
        for (Iterator<Point3i> i = iteratorNonNull(); i.hasNext(); i.next())
            size++;
        return size;
    }
    
    class NonNullIterator implements Iterator<Point3i>
    {
        private Iterator<Point3i> mRootIterator;
        private Point3i mNext;
        
        public NonNullIterator()
        {
            mRootIterator = iterator();
            advance();
        }
        
        private void advance()
        {
            while (mRootIterator.hasNext())
            {
                Point3i n = mRootIterator.next();
                if (contains(n))
                {
                    mNext = n;
                    return;
                }
            }
            mNext = null;
        }

        @Override
        public boolean hasNext()
        {
            return mNext != null;
        }

        @Override
        public Point3i next()
        {
            Point3i next = mNext;
            advance();
            return next;
        }

        @Override
        public void remove()
        {
        }
        
    }
}
