// Agent Explorer

/* Initial beliefs and rules */

// conditions for goal selection
is_energy_goal :- energy(MyE) & maxEnergy(Max) & MyE < Max/3.
//is_probe_goal  :- position(MyV) & not probedVertex(MyV,_) & role(explorer).
is_probe_goal  :- position(MyV) & not jia.is_probed_vertex(MyV) & role(explorer).
is_buy_goal    :- money(M) & M >= 10.
is_move_goal	 :- target(X) & not position(X).
//is_survey_goal :- position(MyV) & edge(MyV,_,unknown).  // some edge to adjacent vertex is not surveyed

/* Initial goals */

//+simStart
//	:	role(explorer)
//	<-	!select_explorer_goal.

+simEnd 
   <- .abolish(_); // clean all BB
      .drop_all_desires.
	  
+!explorer_goal
	<-	.print("Starting explorer_goal");
			!select_explorer_goal.

+!select_explorer_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_explorer_goal.

+!select_explorer_goal
	: is_probe_goal
	<-	!init_goal(probe);
			!!select_explorer_goal.

//+!select_explorer_goal
//	: is_survey_goal
//	<-	!init_goal(survey);
//			!!select_explorer_goal.

+!select_explorer_goal
	:	is_buy_goal
	<-	!init_goal(buy(battery));
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			//!init_goal(random_walk);
			!!select_explorer_goal.

+!select_explorer_goal
	<- 	//!init_goal(random_walk);
		  !init_goal(agents_coordination);
			!!select_explorer_goal.

//-!select_explorer_goal[error_msg(M)]
//	<-	.print("Error ",M);
//			!!select_explorer_goal.
-!select_explorer_goal[error(I),error_msg(M)]
	<-	.print("failure in select_explorer_goal! ",I,": ",M).

+!init_goal(G)
	:	step(S) & position(V) & energy(E) & maxEnergy(Max)
	<-	.print("I am at ",V," (",E,"/",Max,"), the goal for step ",S," is ",G);
      !G.

+!init_goal(G)
	<-	.print("No step yet... wait a bit");
      .wait(300);
	  	!init_goal(G).


/* Plans for energy */

+!be_at_full_charge 
    : energy(MyE) & maxEnergy(M) & MyE > M*0.9 // I am full, nothing to do
   <- .print("Charged at ",MyE).
+!be_at_full_charge 
    : energy(MyE)
   <- .print("My energy is ",MyE,", recharging");
      !do_and_wait_next_step(recharge);
	  	!be_at_full_charge. // otherwise, recharge


/* Probe plans */

+!probe
   <- .print("Probing my location");
      !do_and_wait_next_step(probe).
	  
/* Probe plans */

+!survey
   <- .print("Surveying");
      !do_and_wait_next_step(survey).

/* Buy battery */

+!buy(X) 
    : money(M)
   <- .print("I am going to buy ",X,"! My money is ",M);
      !do_and_wait_next_step(buy(X)).


/* Random walk plans */

+!random_walk 
    : position(MyV) // my location
   <- jia.random_walk(MyV,Target);
   		!do_and_wait_next_step(goto(Target)).

-!random_walk[error(I),error_msg(M)]
	<-	.print("failure in random_walk! ",I,": ",M).


+!agents_coordination
	: step(S)
	<- 	jia.agents_coordination(A,P);
			.print("New formation!! ", .length(P));
			!send_target(A,P);
			!wait_next_step(S).


+!send_target([X|TAg],[Y|TLoc])
 	<- .print("send: ",X, ", " ,Y);
 	   .send(X,tell,target(Y));
 	   //.print("TAg: ", TAg);
 	   !send_target(TAg,TLoc).
+!send_target([],[]).

-!send_target[error(I),error_msg(M)]
	<-	.print("failure in send_target! ",I,": ",M).


+!move_to_target
	:	target(Y) & position(X)
	<-	jia.move_to_target(X,Y,NextPos);
			!do_and_wait_next_step(goto(NextPos)).
-!move_to_target[error(I),error_msg(M)]
	<-	.print("failure in move_to_target! ",I,": ",M).
