// Agent Explorer

/* Initial beliefs and rules */

// conditions for goal selection
is_probe_goal  :- position(MyV) & not jia.is_probed_vertex(MyV) & role(explorer).

/* Initial goals */

+!explorer_goal
	<-	.print("Starting explorer_goal");
			!select_explorer_goal.


+!select_explorer_goal
	:	is_call_help_goal & step(S)
		<-	jia.get_repairers(Agents);
				!init_goal(call_help(Agents));
				+need_help;
				!alert_saboteur;
				!!select_explorer_goal.

+!select_explorer_goal
	:	is_not_need_help_goal
	<-	jia.get_repairers(Agents);
			!init_goal(send_not_need_help(Agents));
			-need_help;
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_disabled_goal & step(S)
	<-	.print("Moving to closest repairer.");
			jia.closer_repairer(Pos);
			!init_goal(move_closer_to_repairer(Pos));
			!!select_explorer_goal.

+!select_explorer_goal
	: is_probe_goal
	<-	!init_goal(probe);
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_buy_goal
	<-	!init_goal(buy(battery));
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			!!select_explorer_goal.

+!select_explorer_goal
	:	is_wait_goal
	<-	-target(_);
			!init_goal(move_to_not_probed);
			!!select_explorer_goal.

+!select_explorer_goal
	<- 	!init_goal(agents_coordination);
			!!select_explorer_goal.

-!select_explorer_goal[error(I),error_msg(M)]
	<-	.print("failure in select_explorer_goal! ",I,": ",M).



/* Plans for energy */

+!be_at_full_charge 
    : energy(MyE) & maxEnergy(M) & MyE > M*0.9 // I am full, nothing to do
   <- .print("Charged at ",MyE).
+!be_at_full_charge 
    : energy(MyE)
   <- .print("My energy is ",MyE,", recharging");
      !do_and_wait_next_step(recharge). // otherwise, recharge


/* Probe plans */

+!probe
   <- .print("Probing my location");
      !do_and_wait_next_step(probe).

/* Buy battery */

+!buy(X) 
    : money(M)
   <- .print("I am going to buy ",X,"! My money is ",M);
      !do_and_wait_next_step(buy(X)).


/* Random walk plans */

+!random_walk 
    : position(MyV) // my location
   <- //jia.random_walk(MyV,Target);
   		jia.least_visited_neighbor(MyV,Target);
   		!do_and_wait_next_step(goto(Target)).

-!random_walk[error(I),error_msg(M)]
	<-	.print("failure in random_walk! ",I,": ",M).


/* Agents coordination plans */

+!agents_coordination
	: step(S)
	<- 	jia.agents_coordination(A,P);
			.print("New formation!! ", .length(P));
			!send_target(A,P);
			!wait_next_step(S).

+!send_target([X|TAg],[Y|TLoc])
 	<- 	.print("send: ",X, ", " ,Y);
 	   	.send(X,tell,target(Y));
 	   	!send_target(TAg,TLoc).
+!send_target([],[]).

-!send_target[error(I),error_msg(M)]
	<-	.print("failure in send_target! ",I,": ",M).


/* Move to taget plans */

+!move_to_target
	:	target(Y) & position(X)
	<-	jia.move_to_target(X,Y,NextPos);
			!do_and_wait_next_step(goto(NextPos)).
-!move_to_target[error(I),error_msg(M)]
	<-	.print("failure in move_to_target! ",I,": ",M).


/* Move to not probed */

+!move_to_not_probed
	: position(MyV) // my location
	<- jia.move_to_not_probed(MyV,Target);
		 !do_and_wait_next_step(goto(Target)).
