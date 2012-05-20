// Agent Marcian

/* Initial beliefs and rules */

role("Marcian").

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

// to create the scheme
+!run_scheme(S)
	<- //makeArtifact(S,"ora4mas.nopl.MySchemeBoard",["wp-os.xml", writePaperSch, false, true ],SchArtId);
			makeArtifact(S,"ora4mas.nopl.SchemeBoard",["agents-on-mars-os.xml", marsSch, false, false ],SchArtId);
			focus(SchArtId);
			.print("scheme ",S," created");
			addScheme(S)[artifact_name("marsGroupBoard")]; 
			.print("scheme is linked to responsible group").
-!run_scheme(S)[error(I),error_msg(M)]
	<- 	.print("failure creating scheme ",S," -- ",I,": ",M).
	
+!playRole
	:	role(R)
	<- adoptRole(marcian)[artifact_id(GrArtId)].