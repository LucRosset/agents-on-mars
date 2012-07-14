// Common plans


/* Initial beliefs and rules */

// conditions for goal selection
is_energy_goal 					:- energy(MyE) & maxEnergy(Max) & MyE < Max/3.
is_buy_goal    					:- money(M) & M >= 10.
is_move_goal	 					:- target(X) & not jia.is_at_target(X).
is_wait_goal	 					:- target(X) & jia.is_at_target(X).
is_call_help_goal 			:- health(0) & not need_help.
is_disabled_goal				:- health(0) & need_help.
is_not_need_help_goal		:- health(X) & maxHealth(X) & need_help.
is_parry_goal 					:- position(X) & jia.has_saboteur_at(X).

/* General plans */

// the following plan is used to send only one action each cycle
+!do_and_wait_next_step(Act)
    : step(S)
   <- Act; // perform the action (i.e., send the action to the simulator)
     !wait_next_step(S). // wait for the next step before going on

+!wait_next_step(S)  : step(S+1).
+!wait_next_step(S) <- .wait( { +step(_) }, 600, _); !wait_next_step(S).

 
+!init_goal(G)
	:	step(S) & position(V) & energy(E) & maxEnergy(Max)
	<-	.print("I am at ",V," (",E,"/",Max,"), the goal for step ",S," is ",G);
      !G.

+!init_goal(G)
	<-	.print("No step yet... wait a bit");
      .wait(300);
	  	!init_goal(G).


//+step(S) <- .print("Current step is ", S).	// used for debug purposes


+simEnd 
   <- .abolish(_); // clean all BB
      .drop_all_desires.



/* Plans */
			
+!call_help([X|TAg])
	: .my_name(Me)
	<-	.print("sending need_help to ",X);
 	   	.send(X,tell,need_help(Me));
 	   	!call_help(TAg).
+!call_help([]).


+!send_not_need_help([X|TAg])
	: .my_name(Me)
	<-	.print("sending not_need_help to ",X);
 	   	.send(X,tell,not_need_help(Me));
 	   	!send_not_need_help(TAg).
+!send_not_need_help([]).

+!alert_saboteur
	: position(X) & jia.has_unique_opponent_at(X,Ag)
	<-	.broadcast(tell,saboteur(Ag,X)).
+!alert_saboteur.

+!move_closer_to_repairer("neighbor")
	:	step(S)
	<-	!wait_next_step(S).

+!move_closer_to_repairer("none")
	: is_move_goal
	<- 	!init_goal(move_to_target);
			!!select_goal.
+!move_closer_to_repairer("none")
	:	is_wait_goal & step(S)
	<-	!wait_next_step(S).
+!move_closer_to_repairer("none")
	<-	!init_goal(random_walk);
			!!select_goal.

+!move_closer_to_repairer(Pos)
	: position(X)
	<-	jia.move_to_target(X,Pos,NextPos);
			!do_and_wait_next_step(goto(NextPos)).

/* parry plan */
+!parry
	<-	!do_and_wait_next_step(parry).