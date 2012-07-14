// Agent Inspector

/* Initial beliefs and rules */

// conditions for goal selection

/* Initial goals */

+!inspector_goal
	<-	.print("Starting inspector_goal");
			!select_inspector_goal.


+!select_inspector_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_inspector_goal.

+!select_inspector_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_inspector_goal.

+!select_inspector_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_inspector_goal.

+!select_inspector_goal
	:	is_disabled_goal & step(S)
	<-	.print("Moving to closest repairer.");
			jia.closer_repairer(Pos);
			!init_goal(move_closer_to_repairer(Pos));
			!!select_inspector_goal.

+!select_inspector_goal
	: is_parry_goal
	<- 	!init_goal(random_walk);
			!!select_inspector_goal.

+!select_inspector_goal
	:	is_move_goal
	<-	!init_goal(move_to_target);
			!!select_inspector_goal.

+!select_inspector_goal
	:	is_wait_goal & step(S)
	<-	.print("waiting next step");
			!wait_next_step(S);
			!!select_inspector_goal.

+!select_inspector_goal
	<- 	!init_goal(random_walk);
			!!select_inspector_goal.