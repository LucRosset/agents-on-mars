// Agent Sentinel

/* Initial beliefs and rules */



// conditions for goal selection

/* Initial goals */

+!sentinel_goal
	<-	.print("Starting sentinel_goal"); 
			!select_sentinel_goal.


+!select_sentinel_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_sentinel_goal.

+!select_sentinel_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
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
	:	is_wait_goal
	<-	!init_goal(wait);
			!!select_sentinel_goal.

+!select_sentinel_goal
	<- 	!init_goal(random_walk);
			!!select_sentinel_goal.
