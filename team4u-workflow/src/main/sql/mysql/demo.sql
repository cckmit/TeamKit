# 待审批列表
select pi.current_node_id,
       pi.current_node_name,
       pi.process_definition_id,
       pi.process_definition_name,
       f.*
from process_instance pi,
     process_assignee pa,
     test_form_index f
where pi.process_instance_id = f.process_instance_id
  and pi.process_instance_id = pa.process_instance_id
  and pi.current_node_id = pa.node_id
  and pa.action_id = ''
  and pa.assignee = '?';

# 历史审批列表
select pi.current_node_id,
       pi.current_node_name,
       pi.process_definition_id,
       pi.process_definition_name,
       f.*
from process_instance pi,
     process_assignee pa,
     test_form_index f
where pi.process_instance_id = f.process_instance_id
  and pi.process_instance_id = pa.process_instance_id
  and pa.action_id != ''
  and pa.assignee = '?';

# 申请列表
select pi.current_node_id,
       pi.current_node_name,
       pi.process_definition_id,
       pi.process_definition_name,
       f.*
from process_instance pi,
     test_form_index f
where pi.process_instance_id = f.process_instance_id
  and pi.create_by = '?';