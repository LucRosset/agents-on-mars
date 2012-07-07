// Agent Saboteur


+!saboteur_goal
	<- 	.print("Starting saboteur_goal");
			!select_saboteur_goal.

+!select_saboteur_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_saboteur_goal.

+!select_saboteur_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			!!select_saboteur_goal.

+!select_saboteur_goal
	:	is_wait_goal & step(S)
	<-	.print("waiting next step");
			!wait_next_step(S);
			!!select_saboteur_goal.

+!select_saboteur_goal
	<- 	!init_goal(random_walk);
			!!select_saboteur_goal.