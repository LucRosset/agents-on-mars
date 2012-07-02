// Agent Explorer

/* Initial beliefs and rules */

// conditions for goal selection
is_energy_goal :- energy(MyE) & maxEnergy(Max) & MyE < Max/3.
is_probe_goal  :- position(MyV) & not probedVertex(MyV,_) & role(explorer).
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
		  !init_goal(coordinate_agents);
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

//+!random_walk 
//    : position(MyV) // my location
//   <- .setof(V, visibleEdge(MyV,V), Options);
//   		if (.length(Options,0)) {
//   			.setof(X, visibleEdge(X,MyV), Optionss);
//   			.nth(math.random(.length(Optionss)), Optionss, Ops);
//   			.print("Random walk options ",Optionss," going to ",Ops);
//	  		!do_and_wait_next_step(goto(Ops))
//   		};
//   		if (not .length(Options,0)) {
//   			.nth(math.random(.length(Options)), Options, Op);
//   			.print("Random walk options ",Options," going to ",Op);
//	  		!do_and_wait_next_step(goto(Op))
//   		}.

+!random_walk 
    : position(MyV) // my location
   <- jia.random_walk(MyV,Target);
   		!do_and_wait_next_step(goto(Target)).

-!random_walk[error(I),error_msg(M)]
	<-	.print("failure in random_walk! ",I,": ",M).


+!coordinate_agents
	: step(S)
	<- 	jia.calculate_strategy(P);
			.findall(coworkerPosition(X,Y), coworkerPosition(X,Y), Agents);
			.print("New formation!! ", .length(P));
			!send_target(Agents,P);
			!wait_next_step(S).

// send a new target(X) to the agents
+!send_target([coworkerPosition(X,Y)|TAg],Positions)
	: .length(Positions) > 0
 	<- !find_closest(coworkerPosition(X,Y),Positions,NearPos);
 		 .print("send: ", X, ", " ,NearPos);
 	   .send(X,tell,target(NearPos));
 	   .print("TLOC: ", .length(Positions));
 	   .print("TAg: ", TAg);
 	   .delete(NearPos,Positions,TLoc);
 	   !send_target(TAg,TLoc).
+!send_target([],Positions).
+!send_target(Agents,[]).
+!send_target([],[]).
+!send_target(_,Positions).
-!send_target[error(I),error_msg(M)]
	<-	.print("failure in send_target! ",I,": ",M).


// find the position near to the coworker agent
+!find_closest(coworkerPosition(_,Y), Positions, NearPos)
 	<- .my_name(Me);
 	   .findall(d(D,Pos),
           .member(Pos,Positions) & jia.path_length(Y,Pos,D), Distances);
           .min(Distances,d(_,NearPos)).
-!find_closest[error_msg(M),code(C),code_line(L)]
	<- .print("Error on find_closest, command: ",C,", line ",L,", message: ",M).


+!move_to_target
	:	target(Y) & position(X)
	<-	jia.move_to_target(X,Y,NextPos);
			!do_and_wait_next_step(goto(NextPos)).
-!move_to_target[error(I),error_msg(M)]
	<-	.print("failure in move_to_target! ",I,": ",M).

/* general plans */

// the following plan is used to send only one action each cycle
+!do_and_wait_next_step(Act)
    : step(S)
   <- Act; // perform the action (i.e., send the action to the simulator)
     !wait_next_step(S). // wait for the next step before going on

+!wait_next_step(S)  : step(S+1).
+!wait_next_step(S) <- .wait( { +step(_) }, 600, _); !wait_next_step(S).

+step(S) <- .print("Current step is ", S).


// store perceived probed vertexs in the BB
//+probedVertex(L,V) <- +probedVertex(L,V). 

// store edges in the BB
//@lve1[atomic]
//+visibleEdge(V1,V2)    
//   <- +edge(V1,V2,unknown);
//      +edge(V2,V1,unknown).
	  
//@lve12[atomic]
//+surveyedEdge(V1,V2,C) 
//   <- -edge(V1,V2,_); -edge(V2,V1,_);
//      +edge(V1,V2,C); +edge(V2,V1,C).
