<?php
$arr = array('st' => 'ok', 'ob' => array(
	array('id' => '1', 'name' => 'Felipe y co.', 'description' => 'Venta de indumentaria'),
	array('id' => '2', 'name' => 'Arzion', 'description' => 'Software desing'),
	array('id' => '3', 'name' => 'Coto', 'description' => 'Supah dupah market'),
	array('id' => '4', 'name' => 'Soho', 'description' => 'Expensive clothes')));

echo json_encode($arr);
?>
