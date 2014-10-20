import java.util.*;

public class list
{
    List<String> block_list;
    
    list()
    {
        block_list = new ArrayList<String>();
    }
    
    boolean is_new_block()
    {
        return( block_list.isEmpty() );
    }
    
    //      block_list.contains(    string .... )  
}

