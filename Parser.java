import java.util.*;
public class Parser {


    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private void scan() {
        tok = scanner.scan();
    }

    
    Stack<list> our_stack = new Stack<list>();
    
    
    private Scan scanner;
    Parser(Scan scanner) {
        this.scanner = scanner;
        scan();
        program();
        if( tok.kind != TK.EOF )
            parse_error("junk after logical end of program");
    }
    
   // -----------------------------------------------------
    
    /*   is_in:
                0 = not within the stack
                1 = within the stack
                2 = redeclaration within block
     */
    int checking( String testing)
    {
        int size = 0;
        list temp = new list();
        list next = new list();
        int is_in = 0;
        
        if( !(our_stack.empty()) ) //if stack isn't empty
        {
            size = our_stack.size();
            Iterator<list> itr = our_stack.iterator();
            
            temp = our_stack.peek();
            
            if( temp.block_list.contains( testing )   )
            {
                is_in = 2;  //set it so that it means it's already within
                
            }//if() redeclared in newest block
            else
            {
               // System.out.println("NEW_______:");
                while( itr.hasNext() )      //traverse through list from old->new
                {
                    next = itr.next();
                    //System.out.println(next.block_list);
                    
                    if( next.block_list.contains( testing ) )
                    {
                        is_in = 1; //True, it's within the stack
                    }
                }//while()
            }//else()
        }
        return is_in;
    }//checking();
    
    //program::=block
    private void program() {
        block();
    }

    //block ::= [declarations] statement_list
    private void block() {
        if( is(TK.VAR) )
        {
            declarations();
        }
        statement_list();
    }

    //declarations::= var {id} rav
    private void declarations() {
        //every time there is a "Var" that means it's a variable declaration
        list new_block = new list();
        our_stack.push(new_block);
        
        mustbe(TK.VAR);
        int checking_declarations = 0;
        list top = new list();
        
        //while there's 'id' inside the variable declaration
        while( is(TK.ID) ) {
            
            checking_declarations = checking(tok.string);
            
            if(checking_declarations ==2)
            {
                System.err.println( "variable "+ tok.string + " is redeclared on line "+ tok.lineNumber);
            }
            else if(checking_declarations == 0)
            {
                top = our_stack.peek();
                top.block_list.add(tok.string);
               //  System.out.println(top.block_list);
            }
            
           
            scan();
        }
        mustbe(TK.RAV); //end of variable declaration
    }
    
    
//statement_list ::= {statement}
    private void statement_list()
    {
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
            //list newish = new list();
           // our_stack.push(newish);
            
          //  System.out.println("PUSHED! -> If() "+ tok.lineNumber);

            
            if_proc();
            
            if(our_stack.empty() ==false){
               // System.out.println("POPPED! -> If() "+ tok.lineNumber);
                our_stack.pop();}
            
        }
        else if( is(TK.DO) )
        {
            do_proc();
            
            //list newy = new list();
            //our_stack.push(newy);
            
            if(our_stack.empty() ==false){
                // System.out.println("POPPED! -> DO() ");
                our_stack.pop();}
        }
        else if( is(TK.FA) )
        {
            // recent
            //list k = new list();
            //our_stack.push(k);
            fa();
            if(our_stack.empty() ==false){
              //    System.out.println("POPPED! -> FA() " );
                our_stack.pop();}
        }
        
    }

    
//assignment::= id ':=' expression
    private void assignment()
    {
        int checking_assignment = 0;
        
       // mustbe(TK.ID);
        if( is(TK.ID) )
        {
            checking_assignment = checking(tok.string);
            if(checking_assignment == 0)
            {
                System.err.println( "undeclared variable "+ tok.string +  " on line "+ tok.lineNumber);
                System.exit(1);
            }
            else
            {
                scan();
                //mustbe(TK.ID);
            }
           
        }
       
        int checking_assign = 0;
        if( is(TK.ASSIGN) )
        {
            mustbe(TK.ASSIGN);
        }
        
        if( is(TK.ID) )
        {
            checking_assign = checking(tok.string);
            if(checking_assign == 0)
            {
                System.err.println( "undeclared variable "+ tok.string +  " on line "+ tok  .lineNumber);
                System.exit(1);
            }
        }
        
        expression();
    }
    
//print::= print expression
    private void print()
    {
        int checking_print = 0;
        mustbe(TK.PRINT);
        //if printing out an id that's undeclared!
        
        
        if( is(TK.ID) )
        {
            /*System.out.println("In print(): "+ tok.lineNumber +tok.string + tok.kind);
            
            list k = new list();
            k = our_stack.peek();
            System.out.println(k.block_list);*/
          
            
            //  System.out.println("Line number: "+tok.lineNumber);
            checking_print = checking(tok.string);
            
            if(checking_print == 0)
            {
                System.err.println( "undeclared variable "+ tok.string +  " on line "+ tok  .lineNumber);
                System.exit(1);
            }
        }

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
        int checking_fa = 0;
        mustbe(TK.FA);
       // mustbe(TK.ID);
        if( is(TK.ID) )
        {
           // System.out.println( "String that's an id w/in fa() : " + tok.string);
            checking_fa = checking(tok.string);
            if(checking_fa == 0)
            {
                System.err.println( "undeclared variable "+ tok.string +  " on line "+ tok.lineNumber);
                System.exit(1);
            }
            else
            {
                scan();
            }

        }
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
        
       // System.out.println("Adding a new block...");
       
        if( !( is(TK.VAR) ) ){list newish = new list();
         our_stack.push(newish);
            
        }
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
