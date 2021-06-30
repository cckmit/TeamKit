/* ProcessInstanceMapper.instancesOfPending */
select pi.*
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

/* ProcessInstanceMapper.instancesOfHistory */
select pi.*
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

/* ProcessInstanceMapper.instancesOfApply */
select pi.*
from process_instance pi
where pi.create_by =             #{query.operator}
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