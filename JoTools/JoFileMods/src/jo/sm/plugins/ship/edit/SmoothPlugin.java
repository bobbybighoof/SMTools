package jo.sm.plugins.ship.edit;

import jo.sm.data.SparseMatrix;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.SmoothLogic;

public class SmoothPlugin implements IBlocksPlugin
{
    public static final String NAME = "Smooth";
    public static final String DESC = "Smooth ships outline with wedges.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_EDIT },
        };

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getDescription()
    {
        return DESC;
    }

    @Override
    public String getAuthor()
    {
        return AUTH;
    }

    @Override
    public Object getParameterBean()
    {
        return null;
    }

    @Override
    public int[][] getClassifications()
    {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p)
    {
       SparseMatrix<Block> modified = new SparseMatrix<Block>(original);
       SmoothLogic.smooth(modified);
        return modified;
    }
}
