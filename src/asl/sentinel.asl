// Agent Sentinel

/* Initial beliefs and rules */

is_parry_goal 					:- 	position(X) & jia.has_saboteur_at(X).

// conditions for goal selection

/* Initial goals */

+!sentinel_goal
	<-	.print("Starting sentinel_goal"); 
			!select_sentinel_goal.

+!select_sentinel_goal
	:	is_call_help_goal & step(S)
		<-	jia.get_repairers(Agents);
				!init_goal(call_help(Agents));
				+need_help;
				!alert_saboteur;
				!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_not_need_help_goal
	<-	jia.get_repairers(Agents);
			!init_goal(send_not_need_help(Agents));
			-need_help;
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_disabled_goal & step(S)
	<-	.print("Moving to closest repairer.");
			jia.closer_repairer(Pos);
			!init_goal(move_closer_to_repairer(Pos));
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_parry_goal
	<-	!init_goal(parry);
			!!select_sentinel_goal.
	
+!select_sentinel_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_wait_goal & step(S)
	<-	.print("waiting next step");
			!wait_next_step(S);
			!!select_sentinel_goal.

+!select_sentinel_goal
	<- 	!init_goal(random_walk);
			!!select_sentinel_goal.

/* parry plan */

+!parry
	<-	!do_and_wait_next_step(parry).