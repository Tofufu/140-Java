import java.util.*;

public class Parser {

    private Token tok;
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
    
    //program::=block
    private void program() {
        block();
        if( our_stack.empty() == false )
        {
            //  donna
            //System.err.println( "POPPED!");
            our_stack.pop();    //every time we exit a block
        }
        }

    //block ::= [declarations] statement_list
    private void block() {
        if( is(TK.VAR) )
        {
            list top = new list();
       //     top.block_list.add(tok.string); //start of a new block
            our_stack.push(top);
            declarations();
        }
        statement_list();
        }

    //declarations::= var {id} rav
    private void declarations() {
        mustbe(TK.VAR);
        
        //---------------------
        list temp = new list(); //this is a reference to the last one via iterator
        list next;
        //list newt = new list();
        int size = 0;
        int is_in=0;
        //----------------------
        
        while( is(TK.ID) )
        {
            //---------------------- ADDED HERE ---------------------------------------
            is_in = 0;
          //  newt = our_stack.peek();
         //   if( newt.block_list.isEmpty() == false)
            
            if( !(our_stack.empty()) ) //if stack isn't empty
            {
               
                size = our_stack.size();
                Iterator<list> itr = our_stack.iterator();
                
                temp = our_stack.peek();
               // System.err.println( "Top Stack: "+temp.block_list);
                
                if( temp.block_list.contains( tok.string )   )
                {
                    System.err.println( "variable "+ tok.string + " is redeclared on line "+ tok.lineNumber);
                }//if() redeclared in newest block
                else
                {
                    while( itr.hasNext() )      //traverse through list from old->new
                    {
                        next = itr.next();
                        
                        if( next.block_list.contains( tok.string ) )
                        {
                            is_in = 1; //True, it's within the stack
                        }
                    }//while()
                    
                    if(is_in == 0)  //if it's not within stack, add the most recent block
                    {
                       
                        temp.block_list.add(tok.string);
                        //  donna
                         //System.err.println( "Things added not w/in stack "+temp.block_list);
                    }
                }//else()
                
            }//if() empty stack
           // else
           // {
              //  newt.block_list.add(tok.string);
              //  list bottom = new list();
               // bottom.block_list.add(tok.string);
                //our_stack.push(bottom);
            //}

            
            //----------------------- ENDED HERE --------------------------------
            scan();
        }
        mustbe(TK.RAV);
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
        
    }

    
//assignment::= id ':=' expression
    private void assignment()
    {
        list check = new list();
        int in = 0;
        
        // donna
         //System.err.println( "Goes back to assignment at " + tok.lineNumber);
        
        if( is(TK.ID) )
        {
            Iterator<list> it2 = our_stack.iterator();

            while( it2.hasNext() )      //traverse through list from old->new
            {
                // donna
                //list testing = new list();
                //testing = our_stack.peek();
                //System.err.println( "In assignment(): "+testing.block_list);
                check = it2.next();
                
                if( check.block_list.contains( tok.string ) )
                {
                    in = 1; //True, it's within the stack
                }
            }//while()
            
            // donna
              //  System.out.println( "Checking number if " + tok.string + " exists " + in);
            
            if(in == 0)
            {
                System.err.println( "undeclared variable "+ tok.string +  " on line "+ tok.lineNumber);
                System.exit(1);

                
            }//not within, must be undeclared

            
            mustbe(TK.ID);
            //scan();
        }
      
        
        if( is(TK.ASSIGN) )
        {
            mustbe(TK.ASSIGN);
            //scan();
        }
        
        
        if( is(TK.ID) )
        {
            int z = 0;
            list checking3 = new list();
           // donna
            //  System.err.println( "If this prints, know it's a id!!" + tok.lineNumber);
            
            Iterator<list> it4 = our_stack.iterator();
            
            while( it4.hasNext() )      //traverse through list from old->new
            {
                // donna
                //list testing = new list();
                //testing = our_stack.peek();
                //System.err.println( "In assignment(): "+testing.block_list);
                checking3 = it4.next();
                
                if( checking3.block_list.contains( tok.string ) )
                {
                    z = 1; //True, it's within the stack
                }
            }//while()
            
            // donna
            //  System.out.println( "Checking number if " + tok.string + " exists " + in);
            
            if(z == 0)
            {
                System.err.println( "undeclared variable "+ tok.string +  " on line "+ tok.lineNumber);
                System.exit(1);
                

            }}
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
        list checks = new list();
        int inside = 0;
        mustbe(TK.FA);
        // donna
        // this is type fa inside the text file qq
         //System.err.println( "If this prints, it's at fa" + tok.lineNumber);
        if( is(TK.ID) )
        {
            Iterator<list> it3 = our_stack.iterator();
            
            while( it3.hasNext() )      //traverse through list from old->new
            {
                checks = it3.next();
                
                if( checks.block_list.contains( tok.string ) )
                {
                    inside = 1; //True, it's within the stack
                }
            }//while()
            

            if(inside == 0)
            {
                System.err.println( "undeclared variable "+ tok.string +  " on line "+ tok.lineNumber);
                System.exit(1);
                
                
            }//not within, must be undeclared
            
            
            mustbe(TK.ID);
            //scan();
        }

        // donna
         //System.err.println( "right before assign " + tok.lineNumber);
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
