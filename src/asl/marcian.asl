// Agent Marcian

// include role plans for agents
{ include("explorer.asl") }
{ include("inspector.asl") }
{ include("repairer.asl") }
{ include("saboteur.asl") }
{ include("sentinel.asl") }

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

// marcian1: create workspace and groupBoard and scheme artifacts
+!start
	:	.my_name(marcian1)
	<- 	createWorkspace("marsWS");
  		joinWorkspace("marsWS",MarsMWsp);
			makeArtifact("marsGroupBoard","ora4mas.nopl.GroupBoard",["agents-on-mars-os.xml", team, false, false ],GrArtId);
	 		setOwner(marcian1);
     	focus(GrArtId);
  		!run_scheme(sch1);
  		!playRole.
-!start[error(I),error_msg(M)]
	: .my_name(marcian1)
	<-	.print("failure in starting! ",I,": ",M).

// if not marcian1: join the created workspace and lookup the org artifacts
+!start 
	<- 	!join;
			lookupArtifact("marsGroupBoard",GrId);
			focus(GrId);
			!playRole.
-!start
	<- 	.wait(100);
			!start.

+!join 
	<- 	.my_name(Me); 
			joinWorkspace("marsWS",_).
-!join
	<- 	.wait(200);
			!join.

// scheme creation
+!run_scheme(S)
	<- //makeArtifact(S,"ora4mas.nopl.MySchemeBoard",["wp-os.xml", writePaperSch, false, true ],SchArtId);
			makeArtifact(S,"ora4mas.nopl.SchemeBoard",["agents-on-mars-os.xml", marsSch, false, false ],SchArtId);
			focus(SchArtId);
			.print("scheme ",S," created");
			addScheme(S)[artifact_name("marsGroupBoard")]; 
			.print("scheme is linked to responsible group").
-!run_scheme(S)[error(I),error_msg(M)]
	<- 	.print("failure creating scheme ",S," -- ",I,": ",M).


// keep focused on schemes my groups are responsible for
+schemes(L)
	<-	for ( .member(S,L) ) {
				lookupArtifact(S,ArtId);
				focus(ArtId)
      }.

// plan to start to play a role
+!playRole
	:	role(R)
	<- 	jia.to_lower_case(R,S);
		  -role(R);
		  +role(S);
			adoptRole(S)[artifact_id(GrArtId)];
			.print("I'll play role ",S).

-!playRole
	<- 	.wait(100);
			!playRole.


// plans to handle obligations
+obligation(Ag,Norm,committed(Ag,Mission,Scheme),Deadline)
    : .my_name(Ag)
   <- .print("I am obliged to commit to ",Mission," on ",Scheme);
      commitMission(Mission)[artifact_name(Scheme)].

+obligation(Ag,Norm,achieved(Scheme,Goal,Ag),DeadLine)
    : .my_name(Ag)
   <- .print("I am obliged to achieve goal ",Goal);
      !Goal[scheme(Scheme)];
      goalAchieved(Goal)[artifact_name(Scheme)].

/* general plans */

// the following plan is used to send only one action each cycle
+!do_and_wait_next_step(Act)
    : step(S)
   <- Act; // perform the action (i.e., send the action to the simulator)
     !wait_next_step(S). // wait for the next step before going on

+!wait_next_step(S)  : step(S+1).
+!wait_next_step(S) <- .wait( { +step(_) }, 600, _); !wait_next_step(S).

+step(S) <- .print("Current step is ", S).


// temporary plans
//+simStart
//	:	not role(explorer)
//	<-	!select_goal.

+!select_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_goal.

+!select_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			//!init_goal(random_walk);
			!!select_goal.

+!select_goal
	<- 	!init_goal(random_walk);
			!!select_goal.