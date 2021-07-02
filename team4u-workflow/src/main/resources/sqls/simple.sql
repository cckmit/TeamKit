/* ProcessInstanceMapper.instancesOfPending */
select pi.id,
       pi.process_instance_id,
       pi.process_instance_type,
       pi.process_instance_name,
       #if(query.withDetail)
       pi.process_instance_detail,
       #end
       pi.process_definition_id,
       pi.process_definition_version,
       pi.current_node_id,
       pi.current_node_name,
       pi.concurrency_version,
       pi.create_by,
       pi.update_by,
       pi.create_time,
       pi.update_time
from process_instance pi,
     process_assignee pa
where pi.process_instance_id = pa.process_instance_id
  and pi.current_node_id = pa.node_id
  and pa.action_id = ''
  and pa.assignee =              #{query.operator}
  #if(query.processInstanceId)
  and pi.process_instance_id =   #{query.processInstanceId}"
  #end
  #if(query.processInstanceType)
  and pi.process_instance_type = #{query.processInstanceType}
  #end
  #if(query.createStartTime)
  and pi.create_time >=          #{query.createStartTime}
  #end
  #if(query.createEndTime)
  and pi.create_time <=          #{query.createEndTime}
  #end
order by id;

/* ProcessInstanceMapper.instancesOfReviewed */
select pi.id,
       pi.process_instance_id,
       pi.process_instance_type,
       pi.process_instance_name,
       #if(query.withDetail)
       pi.process_instance_detail,
       #end
       pi.process_definition_id,
       pi.process_definition_version,
       pi.current_node_id,
       pi.current_node_name,
       pi.concurrency_version,
       pi.create_by,
       pi.update_by,
       pi.create_time,
       pi.update_time
from process_instance pi,
     process_assignee pa
where pi.process_instance_id = pa.process_instance_id
  and pi.current_node_id = pa.node_id
  and pa.action_id != ''
  and pa.assignee =              #{query.operator}
  #if(query.processInstanceId)
  and pi.process_instance_id =   #{query.processInstanceId}"
  #end
  #if(query.processInstanceType)
  and pi.process_instance_type = #{query.processInstanceType}
  #end
  #if(query.createStartTime)
  and pi.create_time >=          #{query.createStartTime}
  #end
  #if(query.createEndTime)
  and pi.create_time <=          #{query.createEndTime}
  #end
order by id desc;

/* ProcessInstanceMapper.allInstances */
select pi.id,
       pi.process_instance_id,
       pi.process_instance_type,
       pi.process_instance_name,
       #if(query.withDetail)
       pi.process_instance_detail,
       #end
       pi.process_definition_id,
       pi.process_definition_version,
       pi.current_node_id,
       pi.current_node_name,
       pi.concurrency_version,
       pi.create_by,
       pi.update_by,
       pi.create_time,
       pi.update_time
from process_instance pi
where 1 = 1
  #if(query.operator)
  and pi.create_by =             #{query.operator}
  #end
  #if(query.processInstanceId)
  and pi.process_instance_id =   #{query.processInstanceId}"
  #end
  #if(query.processInstanceType)
  and pi.process_instance_type = #{query.processInstanceType}
  #end
  #if(query.createStartTime)
  and pi.create_time >=          #{query.createStartTime}
  #end
  #if(query.createEndTime)
  and pi.create_time <=          #{query.createEndTime}
  #end
order by id desc;