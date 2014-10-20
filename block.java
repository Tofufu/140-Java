import java.util.*;


//This class is a block's list within the stack....
//List<Integer> block_list = new ArrayList<Integer>(); is the ARRAY AKA THE LIST
//using block class so we can iterate and check cleaner
public class block{

   List<String> block_list;

    //constructor, sets and makes the new list
  block()
  {
      block_list = new ArrayList<String>();
  }
    
    //HAVE TO USE TOK.STRING TO TEST IN AND PLACE IN
    int check_within_block( String symbol_passed_in )
    {
        int return_num = 0;
        if( block_list.isEmpty())
        {
            return_num = 2;
        }//if it's empty
        else if( block_list.contains( symbol_passed_in ) )
        {
            return_num = 1;
        }//if it's within the current block
        
        return return_num; //else it'll say that it doesn't belong
    }
  
}
