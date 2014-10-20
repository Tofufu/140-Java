import java.util.*; //importing stack and array list


public class Parser {
    
    Stack<block> our_stack = new Stack<block>(); //public since it's in a public class set default
       // Iterator iter = our_stack.iterator(); //   while(iter.hasNext()) -->this actually prints out FIFO
    
    
    
    /*  block testing = new block();
    
    block.create_block_list();          <-- this doesn't work because you can't call methods inside a class     */
    
    private Token tok; // the current token
    
    //List<Integer> block_list = new ArrayList<Integer>();
    
    private void scan() {
        tok = scanner.scan();
    }
    
    
    
    private Scan scanner;

    //constructor for the class
    Parser(Scan scanner) {
        
    
    /*    block testing = new block();             // <-- for references
        testing.block_list.add("hello");
        testing.block_list.add("there");
        testing.block_list.add("why 3");
        
        
        block helping = new block();
        helping.block_list.add("helping");
        
        
        our_stack.push(testing);
        our_stack.push(helping);
      
        int size = our_stack.size();
        ListIterator<block> t2 =our_stack.listIterator(size); //.hasPrevious
       
        while(t2.hasPrevious() ){
            
           // block temp = t2.next();
            
           // System.out.println(temp);
            
          //  temp = t2.next();
            
           // System.out.println(temp);
            block temp  =  t2.previous();
            System.out.println("This should print out twice, 2 things in stack");
           
            for(String s: temp.block_list)
            {
                System.out.println(temp.block_list);
            }
        }
        */
    
       
        this.scanner = scanner;
        scan();
        program();
        if( tok.kind != TK.EOF )
            parse_error("junk after logical end of program");
    }
    
    // -----------------------------------------------------
    
    //program::=block
    private void program() {
        block();
    }
    
    //block ::= [declarations] statement_list
    private void block() {
        if( is(TK.VAR) )
        {
            block new_block = new block();
            
            our_stack.push(new_block);
            declarations();
        }
        statement_list();
        
        if(our_stack.empty() == false){ our_stack.pop(); }
        //  END OF THE BLOCK --> HAVE TO POP OFF THE BLOCK
    }
    
    
/*              DECLARATIONS OF THE ID/VAR          */
    //declarations::= var {id} rav
    private void declarations() {
        block temp = new block(); //will contain our more recent block inside stack
        block next ;
        int in_stack = 0;
        
        int index = our_stack.size();
    
        mustbe(TK.VAR);
        while( is(TK.ID) ) {
            
            index = our_stack.size();
            System.err.println( "SIZE OF OUR STACK AT BUILDING: "+index + " " + tok.string);
            
           
            ListIterator<block> l1 =our_stack.listIterator(index); //looks at current block
            temp = l1.previous();
             System.err.println( "temp list after calling iterator: "+ temp.block_list );
            
            in_stack = temp.check_within_block( tok.string);
            
            if( in_stack == 1 ) //redeclaration
            {
                System.err.println( "variable "+ tok.string + " is redeclared on line "
                                   + tok.lineNumber);
            }
            else if( (in_stack== 0|| in_stack==2) && l1.hasPrevious() )       //else if there's more things on the stack check if it exists
            {
                while(l1.hasPrevious() && (in_stack == 0 || in_stack == 2) )
                {
                    next = l1.previous();
                    in_stack = next.check_within_block( tok.string);//tok.string );
                   /* if( in_stack == 1 )
                    {
                        System.err.println("It's within the stack but not in the most recent block");
                    }*/
                    
                    //part 4
                }
                
                
                if( in_stack == 0)  //if it still is 0 we will add it onto the newest block
                {
                    temp.block_list.add(tok.string);
                    int y = temp.block_list.size();
                    for(int i = 0; i < y; i++)
                    {
                        System.err.println( "IN HUUUURRR ");
                        
                        System.err.println(temp.block_list.get(i));
                    }

                    
            
                }
                
            }//elseif()
            else if( (in_stack == 2 || in_stack ==0) && (l1.hasPrevious()==false))  //new block, no others on the stack
            {
                
               // temp.block_list.add( tok.string);//tok.string );      //if nothingwithin the stack
                //block less = new block();
                int y = temp.block_list.size();
                block less = new block();
                String t;
                for(int i = 0; i < y; i++)
                {
                    t = null;
                    System.err.println( "IN HERE IN HERE IN HERE IN HERE IN HERE ");
                    t = temp.block_list.get(i);
                    less.block_list.add(t);
                    
                    System.err.println("These two should be the same: "+ less.block_list.get(i) + temp.block_list.get(i));
                }

                less.block_list.add(tok.string);
                //less.block_list = temp.block_list();
                
               // less.block_list.add(tok.string);
               // our_stack.push(less);
               
            
                
                //System.err.println( "IN HERE IN HERE IN HERE IN HERE IN HERE ");
                
            }
            our_stack.push(temp);
            
            
            
          //  our_stack.pop();    //empties out previous (aka the original)
           // our_stack.push(temp);//replace it with the edited, if nothing was changed and the variable exists already we're just putting in same thing
          
            
            /*
                      Iterator->stack:    store first list into temp;
                        temp->check():      if true->redeclared , print out error msg
                                            else->  add to table
             */
            
            
            scan();
        }
        
        
        //push block onto the stack, done with declarations of this block.
        mustbe(TK.RAV);
    }
    
    
    //statement_list ::= {statement}
    private void statement_list()
    {
        int indexw = our_stack.size();
        block tems = new block();
        ListIterator<block> l3 =our_stack.listIterator(indexw);
        while(l3.hasPrevious())
        {
            tems = l3.previous();
            System.out.println(tems.block_list);
        }

        
        while( is(TK.ID) || is(TK.PRINT) || is(TK.IF) ||  is(TK.DO) || is(TK.FA) )
        {
            statement();
        }
        
    }
    
    //statement ::= assignment | print | if | do | fa
    private void statement()
    {
        if( is(TK.ID) )
        {
            assignment();
        }
        else if( is(TK.PRINT) )
        {
            print();
        }
        else if( is(TK.IF) )
        {
            if_proc();
        }
        else if( is(TK.DO) )
        {
            do_proc();
        }
        else if( is(TK.FA) )
        {
            fa();
        }
        // else
        //{
        //   parse_error("Error in statement()");
        // }
        
    }
    
    
    //assignment::= id ':=' expression
    private void assignment()
    {
        
        System.err.println( "i'm in assingment() right now: "+tok.string);
        block temporary = new block(); //will contain our more recent block inside stack
       // block next;
        int checking = 0;
        
        int size = our_stack.size();
        

        if( is(TK.ID) )
        {
            
            size = our_stack.size();
            ListIterator<block> l2 =our_stack.listIterator(size); //looks at current block
            System.err.println( "After the declaration of irator: "+tok.string);
            System.err.println( "size of our stack atm: "+size);
            System.err.println( "is there a previous in l2 "+l2.hasPrevious());
            while(l2.hasPrevious())
            {
                temporary = l2.previous();
                System.err.println( "While loop: "+tok.string);
                checking = temporary.check_within_block( tok.string );
                if(checking == 1)
                {
                    System.err.println( "This should be the same as the top: "+tok.string);
                    System.err.println( "This checking since we're declared"+checking);
                    break; //break out of while loop because it exists
                }
            }
            
            
            if(checking == 0 | checking == 2)
            {
                
                System.err.println( "This checking since we're undeclared "+checking);
                System.err.println( "mine:: undeclared variable "+ tok.string +  "on  line "+ tok.lineNumber);
                System.exit(1);
            }
            
            
            /*          Can't assign a variable if it isn't declared, this is where we check if the id has been declared within the table!!!
                        
                        Iterator:stack  ->temp gets block
                        temp:checks_if_within:          if variable within the whole stack but not whole block it still exists 
                                                        if false ->print out error message and exit */
            mustbe(TK.ID);
            //scan();
        }
        //else
        // {
        // parse_error("no id in assignment()");
        //}
        
        if( is(TK.ASSIGN) )
        {
            mustbe(TK.ASSIGN);
            //scan();
        }
        // else
        //{
        //   parse_error("no assign in assingment()");
        //}
        
        expression();
    }
    
    //print::= print expression
    private void print()
    {
        mustbe(TK.PRINT);
        expression();
    }
    
    //if::= if guarded_commands fi
    private void if_proc()
    {
        mustbe(TK.IF);
        guarded_commands();
        mustbe(TK.FI);
    }
    
    //do::= do guarded_commands od
    private void do_proc()
    {
        mustbe(TK.DO);
        guarded_commands();
        mustbe(TK.OD);
    }
    
    // fa::= fa id':=' expression to expression [st expression] commands af
    private void fa()
    {
        block temporary = new block(); //will contain our more recent block inside stack
        // block next;
        int checking = 0;
        
        int size = our_stack.size();
        
        mustbe(TK.FA);
        if( is(TK.ID))
        {
     /*       size = our_stack.size();
            ListIterator<block> l2 =our_stack.listIterator(size); //looks at current block
            while(l2.hasPrevious())
            {
                temporary = l2.previous();
                checking = temporary.check_within_block( tok.string );
                if(checking == 1)
                {
                    break; //break out of while loop because it exists
                }
            }
            
            if(checking == 0 | checking == 2)
            {
                   System.err.println( "MINE:: undeclared variable "+ tok.string +  "on  line "+ tok.lineNumber);
            }
*/
            mustbe(TK.ID);
        }
       // mustbe(TK.ID);
        mustbe(TK.ASSIGN);
        expression();
        
        mustbe(TK.TO);
        
        expression();
        
        if( is(TK.ST) )
        {
            mustbe(TK.ST);
            expression();
        }
        
        
        commands();
        mustbe(TK.AF);
    }
    
    //guarded_commands::= guarded_command { '[]' guarded_command } [else commands]
    private void guarded_commands()
    {
        guarded_command();
        while( is(TK.BOX) )
        {
            mustbe(TK.BOX);
            guarded_command();
        }
        
        if( is(TK.ELSE) )
        {
            mustbe(TK.ELSE);
            commands();
        }
    }
    
    //guarded_command::= expression commands
    private void guarded_command()
    {
        expression();
        commands();
    }
    
    //commands ::= '->' block
    private void commands()
    {
        if( is(TK.ARROW) )
        {
            mustbe(TK.ARROW);
            //scan();
        }
        //   else
        //  {
        //     parse_error("no arrow in commands()");
        // }
        
        block();
    }
    
    //  expression:: = simple [relop simple]
    private void expression()
    {
        simple();
        
        if( is(TK.EQ) || is(TK.LT) ||  is(TK.GT) || is(TK.NE) || is(TK.LE) || is(TK.GE) )
        {
            relop();
            simple();
        }
        
    }
    
    //  simple::= term { addop term }
    private void simple()
    {
        
        
        //System.err.println( "IS IT ENTERING SIMPLE???? "+ tok.string + " " + tok.kind);
        
        term();
        while( is(TK.PLUS) || is(TK.MINUS) )
        {
            addop();
            term();
            
        }
        
        
    }
    
    //term::= factor { multop factor }
    private void term()
    {
        factor();
        // System.err.println( "passed factor(): "+ tok.string + " " + tok.kind);
        while( is( TK.TIMES) || is(TK.DIVIDE) )
        {
            multop();
            factor();
            
        }
        
        
    }
    
    // factor::= '(' expression ')' | id | number
    private void factor()
    {
        if( is(TK.LPAREN) )
        {
            mustbe(TK.LPAREN);
            expression();
            mustbe(TK.RPAREN);
        }
        else if( is(TK.ID) )
        {
            mustbe(TK.ID);
        }
        else if( is(TK.NUM) )
        {
            mustbe(TK.NUM);
        }
        else
        {
            
            parse_error("factor");
        }
    }
    
    
    // relop ::= = | < | > | /= | <= | >=
    private void relop()
    {
        if( is(TK.EQ) || is(TK.LT) ||  is(TK.GT) || is(TK.NE) || is(TK.LE) || is(TK.GE) )
        {
            scan();
        }
        else
        {
            parse_error("no symbols in relop()");
        }
    }
    
    
    // addop::= + | -
    private void addop()
    {
        if( is(TK.PLUS) )
        {
            // mustbe(TK.PLUS);
            scan();
        }
        else if( is(TK.MINUS) )
        {
            //   mustbe(TK.MINUS);
            scan();
        }
        else
        {
            parse_error("no symbols in addop()");
        }
    }
    
    // multop::= * | /
    private void multop()
    {
        if( is(TK.TIMES) )
        {
            //mustbe(TK.TIMES);
            scan();
        }
        else if( is(TK.DIVIDE) )
        {
            //  mustbe(TK.DIVIDE);
            scan();
        }
        else
        {
            //   System.err.println( tok.string );
            parse_error("multop");
        }
    }
    //---------------------------------------------------------------------------
    // is current token what we want?
    private boolean is(TK tk) {
        return tk == tok.kind;
    }
    
    // ensure current token is tk and skip over it.
    private void mustbe(TK tk) {
        
        if( ! is(tk) ) {
            System.err.println( "mustbe: want " + tk + ", got " +
                               tok);
            parse_error( "missing token (mustbe)" );
        }
        scan();
    }
    
    private void parse_error(String msg) {
        System.err.println( "can't parse: line "
                           + tok.lineNumber + " " + msg );
        System.exit(1);
    }
}
